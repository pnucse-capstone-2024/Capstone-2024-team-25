import AnswersGridLayout from '../AnswersGridLayout';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ReadingTwoProblemTypeThree({ config, getter, setter, editmode, userSelected, setUserSelected }) {
  return (
    <>
      {config.questions.map(({ questionNumber, example, score, answers }, index) => (
        <div key={questionNumber}>
          {index === 0 && <QuestionHeader subItem="※" title={config.problem} size="lg" />}
          <QuestionHeader
            subItem={`${questionNumber}.`}
            title={
              <>
                <div className="mb-2">({score}점)</div>
                <img src={example} alt={`${questionNumber} img`} />
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
              mode="inLine"
            />
          </QuestionContent>
        </div>
      ))}
    </>
  );
}

export default ReadingTwoProblemTypeThree;
