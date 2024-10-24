/* eslint-disable no-restricted-syntax */
/* eslint-disable no-lone-blocks */
import { useState } from 'react';

const makeConfigList = (config) => {
  const problems = config
    ? Object.entries(config)
        .map(([, value]) => value)
        .reduce((acc, cur) => acc.concat(cur), [])
    : [];

  const questions = problems?.reduce((acc, cur) => acc.concat(cur.questions), []);

  const answers = questions?.reduce((acc, cur) => acc.concat(cur.answers), []);

  return { problems, questions, answers };
};

const useEditExam = (initConfig) => {
  const [texts, setTexts] = useState(makeConfigList(initConfig));
  const [origin, setOrigin] = useState(initConfig);

  const setExam = (config) => {
    setTexts(makeConfigList(config));
    setOrigin(config);
  };

  const getProblem = (problemId) => {
    const { problems } = texts;
    const problem = problems?.find((p) => p.problemId === problemId);
    return problem
      ? {
          id: problem.problemId,
          type: 'problem',
          example: problem.example,
        }
      : null;
  };

  const getOriginProblem = (problemId) => {
    const { problems } = makeConfigList(origin);
    const problem = problems?.find((p) => p.problemId === problemId);
    return problem
      ? {
          id: problem.problemId,
          type: 'problem',
          example: problem.example,
        }
      : null;
  };

  const setProblem = (problemId, example) => {
    const prevProblem = getProblem(problemId);
    const newProblems = texts.problems.map((p) =>
      p.problemId === problemId ? { ...p, example: { ...(prevProblem?.example || {}), ...(example || {}) } } : p,
    );
    setTexts({ ...texts, problems: newProblems });
  };

  const getQuestion = (questionId) => {
    const { questions } = texts;
    const question = questions?.find((q) => q.questionId === questionId);
    return question
      ? {
          id: question.questionId,
          type: 'question',
          example: question.example,
          question: question.question,
        }
      : null;
  };

  const getOriginQuestion = (questionId) => {
    const { questions } = makeConfigList(origin);
    const question = questions?.find((q) => q.questionId === questionId);
    return question
      ? {
          id: question.questionId,
          type: 'question',
          example: question.example,
          question: question.question,
        }
      : null;
  };

  const setQuestion = (questionId, { example, question }) => {
    const prevQuestion = getQuestion(questionId);
    const newQuestions = texts.questions.map((q) =>
      q.questionId === questionId
        ? { ...q, example: example || prevQuestion.example, question: question || prevQuestion.question }
        : q,
    );
    setTexts({ ...texts, questions: newQuestions });
  };

  const getAnswer = (answerId) => {
    const { answers } = texts;
    const answer = answers?.find((a) => a.answerId === answerId);
    return answer
      ? {
          id: answer.answerId,
          type: 'answer',
          answer: answer.answer,
        }
      : null;
  };

  const getOriginAnswer = (answerId) => {
    const { answers } = makeConfigList(origin);
    const answer = answers?.find((a) => a.answerId === answerId);
    return answer
      ? {
          id: answer.answerId,
          type: 'answer',
          answer: answer.answer,
        }
      : null;
  };

  const setAnswer = (answerId, answer) => {
    const newAnswers = texts.answers.map((a) => (a.answerId === answerId ? { ...a, answer } : a));
    setTexts({ ...texts, answers: newAnswers });
  };

  const getEditedConfig = () => {
    if (!origin) return [];
    const editedConfig = [];
    for (const [, pbValue] of Object.entries(origin)) {
      for (const pb of pbValue) {
        let pbExample = getProblem(pb.problemId).example;
        if (typeof pbExample !== 'string') {
          pbExample = JSON.stringify(pbExample);
        }
        const newPb = {
          problemId: pb.problemId,
          example: pbExample,
          questions: [],
        };
        for (const q of pb.questions) {
          let qExample = getQuestion(q.questionId).example;
          if (typeof qExample !== 'string') {
            qExample = JSON.stringify(qExample);
          }
          const newQ = {
            questionId: q.questionId,
            example: qExample,
            question: getQuestion(q.questionId).question,
            answers: [],
          };
          for (const a of q.answers) {
            newQ.answers.push({
              answerId: a.answerId,
              answer: getAnswer(a.answerId).answer,
            });
          }
          newPb.questions.push(newQ);
        }
        editedConfig.push(newPb);
      }
    }
    return editedConfig;
  };

  return {
    texts,
    setExam,
    getter: {
      problem: getProblem,
      question: getQuestion,
      answer: getAnswer,
    },
    setter: {
      problem: setProblem,
      question: setQuestion,
      answer: setAnswer,
    },
    originGetter: {
      problem: getOriginProblem,
      question: getOriginQuestion,
      answer: getOriginAnswer,
    },
    getEditedConfig,
  };
};

export default useEditExam;
