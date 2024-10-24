import Border from './Border';

function Example({ exampleQuestions, exampleAnswers, exampleAnswerSelected, mode }) {
  if (!exampleQuestions || !exampleAnswers) return null;

  const getUnicode = (index) => String.fromCharCode(9312 + index);

  const classNameForWidth = (() => {
    if (mode === 'fullWidth') return 'w-full';
    if (mode === 'inLine') return 'w-1/4';
    return 'w-1/2';
  })();
  return (
    <Border>
      <div className="mb-2 flex w-full justify-center text-charcoalGray text-opacity-70">&lt;보기&gt;</div>
      <div className="mb-2">
        {exampleQuestions.map((exampleQuestion) => (
          <div key={exampleQuestion} className="flex font-semibold">
            {exampleQuestion}
          </div>
        ))}
      </div>
      <div className="flex h-20 w-full flex-wrap items-center">
        {exampleAnswers.map((answer, index) => (
          <div
            key={answer}
            className={`${classNameForWidth} ${exampleAnswerSelected === index + 1 ? 'font-medium underline' : ''}`}
          >
            {`${getUnicode(index)} ${answer}`}
          </div>
        ))}
      </div>
    </Border>
  );
}

export default Example;
