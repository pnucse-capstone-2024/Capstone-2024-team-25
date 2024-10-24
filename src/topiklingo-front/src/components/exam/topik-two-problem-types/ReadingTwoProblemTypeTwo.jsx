import AnswersGridLayout from '../AnswersGridLayout';
import TextArea from '../exam-span/TextArea';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ReadingTwoProblemTypeTwo({ config, getter, setter, editmode, userSelected, setUserSelected }) {
  return (
    <>
      {config.questions.map(({ questionId, questionNumber, question, score, answers }, index) => (
        <div key={questionNumber}>
          {index === 0 && <QuestionHeader subItem="※" title={config.problem} size="lg" />}
          <QuestionHeader
            subItem={`${questionNumber}.`}
            title={
              <>
                <TextArea
                  className="whitespace-pre-wrap"
                  value={getter?.question(questionId).question}
                  onChange={(e) => {
                    setter?.question(questionId, { question: e.target.value });
                  }}
                  editmode={editmode}
                >
                  {question}
                </TextArea>
                <span className="ml-1">({score}점)</span>
              </>
            }
          />
          <QuestionContent>
            <AnswersGridLayout
              answers={answers}
              getter={getter}
              setter={setter}
              editmode={editmode}
              selected={userSelected[questionNumber]}
              setSelected={(number) => {
                const newSelected = [...userSelected];
                newSelected[questionNumber] = number;
                setUserSelected(() => newSelected);
              }}
            />
          </QuestionContent>
        </div>
      ))}
    </>
  );
}

export default ReadingTwoProblemTypeTwo;
