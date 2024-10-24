import useGetUser from '@hooks/useGetUser';
import { useMutation, useQuery } from '@tanstack/react-query';

const examKeys = {
  getExamKey: (id) => ['getExam', id],
  getOriginExamKey: (id) => ['getOriginExam', id],
  getExamAnswerKey: ['getExamAnswer'],
};

const exampleParser = (json) => {
  if (!json || json === 'null' || json === '') return null;

  if (json.startsWith('http')) return json.replace('https://topikkorea.s3.amazonaws.com', '/s3-bucket');

  if (json.startsWith('/s3-bucket')) return json;

  try {
    const test = JSON.parse(json);
    return test;
  } catch (e) {
    try {
      return JSON.parse(
        json
          .replaceAll('"conversation"', "'conversation'")
          .replaceAll('"answers"', "'answers'")
          .replaceAll('"selected"', "'selected'")
          .replaceAll(/\\U[a-fA-F0-9]{8}/g, '')
          .replaceAll('"', '')
          .replaceAll("'", "'")
          .replaceAll(/(?<!\\)'/g, '"')
          .replaceAll('NaN', 'null')
          .replaceAll('nan', 'null')
          .replaceAll('None', 'null'),
      );
    } catch (error) {
      if (json.startsWith('[[')) {
        return JSON.parse(
          json.replaceAll("'", '"').replaceAll('，"', '，‘').replaceAll('""', '"‘').replaceAll('박물지"', '박물지’'),
        );
      }
      return JSON.parse(
        json
          .replaceAll('"conversation"', "'conversation'")
          .replaceAll('"answers"', "'answers'")
          .replaceAll('"selected"', "'selected'")
          .replaceAll(/\\U[a-fA-F0-9]{8}/g, '')
          .replaceAll('"', '')
          .replaceAll("'", "'")
          .replaceAll(/(?<!\\)'/g, '"')
          .replaceAll('NaN', 'null')
          .replaceAll('nan', 'null')
          .replaceAll('None', 'null')
          .replaceAll('"[', '["')
          .replaceAll(']"', '"]')
          .replaceAll("'[", '[')
          .replaceAll("]'", ']')
          .replaceAll("'", '"')
          .replaceAll('\\"', '‘'),
      );
    }
  }
};
const questionParser = (obj) => ({
  ...obj,
  example: exampleParser(obj.example),
});

const problemParser = (obj) => ({
  ...obj,
  example: exampleParser(obj.example),
  questions: obj.questions.map(questionParser),
});

const useGetExamQuery = (id) => {
  const user = useGetUser();
  const fetcher = () =>
    fetch(`${import.meta.env.VITE_API_URL}/exam/${id}`, {
      headers: {
        Authorization: `Bearer ${user?.token}`,
      },
    })
      .then((res) => res.json())
      .then((json) => {
        console.log(json);
        // if json has code key then it is an error
        if (json.code === 404901) {
          const updatedJson = { ...json, message: 'User Credit Not Enough. Please contact the administrator.' };
          return updatedJson;
        }
        const result = { ...json };
        Object.entries(json.config).forEach(([key, value]) => {
          result.config[key] = value.map(problemParser);
        });
        return result;
      });

  return useQuery({
    queryKey: examKeys.getExamKey(id),
    queryFn: fetcher,
  });
};

const useGetExamAnswerQuery = (id) => {
  const user = useGetUser();
  const fetcher = () =>
    fetch(`${import.meta.env.VITE_API_URL}/exam-answer/${id}`, {
      headers: {
        Authorization: `Bearer ${user?.token}`,
      },
    })
      .then((res) => res.json())
      .then((json) => {
        if (json.error) {
          throw new Error(json.error);
        } else {
          return json;
        }
      });

  return useQuery({
    queryKey: examKeys.getExamAnswerKey,
    queryFn: fetcher,
  });
};

const usePostGenerateExamMutate = () => {
  const user = useGetUser();
  const fetcher = ({ title, problems }) =>
    fetch(`${import.meta.env.VITE_API_URL}/exam/generated`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user?.token}`,
      },
      body: JSON.stringify({ title, problems }),
    })
      .then((res) => res.json())
      .then((json) => json.examId);

  return useMutation({
    mutationFn: fetcher,
  });
};

const getAnswerInfo = async (id, token) => {
  try {
    const res = await fetch(`${import.meta.env.VITE_API_URL}/exam-answer/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      const json = await res.json();
      return { answerJson: json };
    }
    return null;
  } catch (err) {
    console.log(err);
    alert('Data loading failed. Please try again.');
    return null;
  }
};

const getExamInfo = async (id, token) => {
  try {
    const res = await fetch(`${import.meta.env.VITE_API_URL}/exam/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (res.ok) {
      const json = await res.json();
      const parsedJson = { ...json };

      Object.entries(json.config).forEach(([key, value]) => {
        parsedJson.config[key] = value.map(problemParser);
      });

      return {
        examId: parsedJson.examId,
        year: parsedJson.year,
        totalQuestions: parsedJson.totalQuestions,
        type: parsedJson.type,
        config: parsedJson.config,
        listenUrl: parsedJson.listenUrl,
      };
    }
    return { examId: null, year: null, totalQuestion: null, type: null, config: null, listenUrl: null };
  } catch (err) {
    alert('Data loading failed. Please try again.');
    return { examId: null, year: null, totalQuestion: null, type: null, config: null, listenUrl: null };
  }
};

const useEditExamMutate = () => {
  const user = useGetUser();
  const fetcher = ({ examId, problems }) =>
    fetch(`${import.meta.env.VITE_API_URL}/exam/${examId}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user?.token}`,
      },
      body: JSON.stringify({ problems }),
    });

  return useMutation({
    mutationFn: fetcher,
  });
};

const usePostExamAnswerMutate = () => {
  const user = useGetUser();
  const fetcher = ({ examId, memberAnswers, realAnswers, score, memberAnalyzeRequest }) =>
    fetch(`${import.meta.env.VITE_API_URL}/exam-answer/${examId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${user?.token}`,
      },
      body: JSON.stringify({ memberAnswers, realAnswers, score, memberAnalyzeRequest }),
    });

  return useMutation({
    mutationFn: fetcher,
  });
};

export {
  useGetExamQuery,
  useGetExamAnswerQuery,
  usePostGenerateExamMutate,
  getExamInfo,
  getAnswerInfo,
  useEditExamMutate,
  usePostExamAnswerMutate,
};
