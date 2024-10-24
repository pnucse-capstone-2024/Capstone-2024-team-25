import AnswersGridLayout from '../AnswersGridLayout';
import Border from '../Border';
import TextArea from '../exam-span/TextArea';
import QuestionContent from '../QuestionContent';
import QuestionHeader from '../QuestionHeader';

function ReadingTwoProblemTypeFive({ config, getter, setter, editmode, userSelected, setUserSelected }) {
  return (
    <>
      {config.questions.map(({ questionId, questionNumber, example, score, answers }, index) => (
        <div key={questionNumber}>
          {index === 0 && <QuestionHeader subItem="※" title={config.problem} size="lg" />}
          <QuestionHeader
            subItem={`${questionNumber}.`}
            title={
              <>
                <div className="mb-2">({score}점)</div>
                <Border isThin>
                  {example?.[0].map((question, i) => (
                    <TextArea
                      key={question}
                      className="whitespace-pre-wrap"
                      value={getter?.question(questionId).example[0][i]}
                      onChange={(e) => {
                        setter?.question(questionId, {
                          example: [
                            example[0].map((q, j) =>
                              i === j ? e.target.value : getter?.question(questionId).example[0][j],
                            ),
                            example[1],
                          ],
                        });
                      }}
                      editmode={editmode}
                    >
                      {question}
                    </TextArea>
                  ))}
                </Border>
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

export default ReadingTwoProblemTypeFive;
