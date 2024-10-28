import AnswersGridLayout from '../AnswersGridLayout';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ListenTwoProblemTypeFive({ config, userSelected, setUserSelected }) {
  return (
    <>
      {config.questions.map(({ questionNumber, question, score, answers }, index) => (
        <div key={questionNumber}>
          {index === 0 && (
            <QuestionHeader
              subItem="※"
              title={<span dangerouslySetInnerHTML={{ __html: config.problem }} />}
              size="lg"
            />
          )}
          <QuestionHeader subItem={`${questionNumber}.`} title={`${question} (${score}점)`} />
          <QuestionContent>
            <AnswersGridLayout
              answers={answers}
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

export default ListenTwoProblemTypeFive;
