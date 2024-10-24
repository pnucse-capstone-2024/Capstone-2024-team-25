import AnswersGridLayout from '../AnswersGridLayout';
import Border from '../Border';
import TextArea from '../exam-span/TextArea';
import Example from '../Example';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ReadingOneProblemTypeOne({ config, getter, setter, editmode, userSelected, setUserSelected }) {
  return (
    <>
      <div className="mb-4">
        <QuestionHeader subItem="※" title={<span className="whitespace-pre-wrap">{config.problem}</span>} size="lg" />
        <QuestionContent>
          <Example
            exampleQuestions={config.example.conversation}
            exampleAnswers={config.example.answers}
            exampleAnswerSelected={config.example.selected}
            mode="inLine"
          />
        </QuestionContent>
      </div>
      {config.questions.map(({ questionId, questionNumber, example, score, answers }) => (
        <div key={questionNumber}>
          <QuestionHeader
            subItem={`${questionNumber}.`}
            title={
              <>
                <div className="mb-2">({score}점)</div>
                <Border isThin>
                  <TextArea
                    className="whitespace-pre-wrap"
                    value={getter?.question(questionId).example[0][0]}
                    onChange={(e) => {
                      setter?.question(questionId, { example: [[e.target.value]] });
                    }}
                    editmode={editmode}
                  >
                    {example}
                  </TextArea>
                </Border>
              </>
            }
          />
          <QuestionContent>
            <AnswersGridLayout
              answers={answers}
              editmode={editmode}
              getter={getter}
              setter={setter}
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

export default ReadingOneProblemTypeOne;
