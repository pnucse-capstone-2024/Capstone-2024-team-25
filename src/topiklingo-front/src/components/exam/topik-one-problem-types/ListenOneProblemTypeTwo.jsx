import AnswersGridLayout from '../AnswersGridLayout';
import Example from '../Example';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ListenOneProblemTypeTwo({ config, userSelected, setUserSelected }) {
  return (
    <>
      <div className="mb-4">
        <QuestionHeader subItem="※" title={config.problem} size="lg" />
        <QuestionContent>
          <Example
            exampleQuestions={config.example.conversation}
            exampleAnswers={config.example.answers}
            exampleAnswerSelected={config.example.selected}
          />
        </QuestionContent>
      </div>
      {config.questions.map(({ questionNumber, question, score, answers }) => (
        <div key={questionNumber}>
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
            />
          </QuestionContent>
        </div>
      ))}
    </>
  );
}

export default ListenOneProblemTypeTwo;
