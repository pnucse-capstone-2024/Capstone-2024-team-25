import AnswerButton from './AnswerButton';

function AnswersGridLayout({ answers, selected, setSelected, img, mode, editmode = false, getter, setter }) {
  if (img) {
    return (
      <div className="my-2 flex w-full flex-wrap">
        {answers &&
          answers.map((answer, index) => (
            <AnswerButton
              key={answer.answerId}
              number={index + 1}
              selected={selected === index + 1}
              handleClick={() => setSelected(index + 1)}
              editmode={editmode}
            >
              <img
                src={
                  answer.answer && answer.answer.startsWith('https:')
                    ? answer.answer.replace('https://topikkorea.s3.amazonaws.com', '/s3-bucket')
                    : answer.answer
                }
                alt={`img ${index}`}
              />
            </AnswerButton>
          ))}
      </div>
    );
  }

  return (
    <div className="my-2 flex w-full flex-wrap">
      {answers &&
        answers.map((answer, index) => (
          <AnswerButton
            key={answer.answerId}
            number={index + 1}
            selected={selected === index + 1}
            handleClick={() => setSelected(index + 1)}
            isFullWidth={mode === 'fullWidth'}
            isInLine={mode === 'inLine'}
            editmode={editmode}
            answerId={answer.answerId}
            getter={getter}
            setter={setter}
          >
            {answer.answer}
          </AnswerButton>
        ))}
    </div>
  );
}

export default AnswersGridLayout;
