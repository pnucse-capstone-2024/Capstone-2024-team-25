import AnswersGridLayout from '../AnswersGridLayout';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ReadingOneProblemTypeSeven({ config, getter, setter, editmode, userSelected, setUserSelected }) {
  return (
    <>
      <div className="mb-4">
        <QuestionHeader subItem="※" title={config.problem} size="lg" />
        <QuestionContent>
          <img src={config.example} alt={`${config.question} img`} />
        </QuestionContent>
      </div>
      {config.questions.map(({ questionNumber, question, score, answers }) => (
        <div key={questionNumber}>
          <QuestionHeader
            subItem={`${questionNumber}.`}
            title={
              <>
                <span className="whitespace-pre-wrap">{question}</span>
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
              mode="fullWidth"
            />
          </QuestionContent>
        </div>
      ))}
    </>
  );
}

export default ReadingOneProblemTypeSeven;
