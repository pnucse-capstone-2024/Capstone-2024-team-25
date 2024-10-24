import AnswersGridLayout from '../AnswersGridLayout';
import Example from '../Example';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ListenOneProblemTypeFive({ config, userSelected, setUserSelected }) {
  const { example } = config;

  return (
    <>
      {example && (
        <div className="mb-4">
          <QuestionHeader subItem="※" title={config.problem} size="lg" />
          <QuestionContent>
            <Example
              exampleQuestions={example.conversation}
              exampleAnswers={example.answers}
              exampleAnswerSelected={example.selected}
            />
          </QuestionContent>
        </div>
      )}
      {config.questions.map(({ questionNumber, question, score, answers }, index) => (
        <div key={questionNumber}>
          {!example && index === 0 && <QuestionHeader subItem="※" title={config.problem} size="lg" />}
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

export default ListenOneProblemTypeFive;
