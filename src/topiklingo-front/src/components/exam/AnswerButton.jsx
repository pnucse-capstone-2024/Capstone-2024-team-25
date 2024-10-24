import Input from './exam-span/Input';

const numberString = {
  1: '①',
  2: '②',
  3: '③',
  4: '④',
};

function AnswerButton({
  number,
  children,
  handleClick,
  selected,
  isFullWidth,
  isInLine,
  editmode = false,
  answerId,
  getter,
  setter,
}) {
  const classNameForWidth = (() => {
    if (isFullWidth) return 'w-full';
    if (isInLine) return 'w-1/4';
    return 'w-1/2';
  })();

  return (
    <div className={classNameForWidth}>
      <button
        type="button"
        name={`answer-${number}`}
        onClick={handleClick}
        className={`mb-1 flex w-full place-items-center rounded px-1 text-start hover:bg-midnightBlue hover:text-platinumWhite ${selected ? 'bg-midnightBlue text-platinumWhite' : null}`}
      >
        <Input
          number={numberString[number]}
          className={selected ? 'underline' : null}
          value={getter?.answer(answerId).answer}
          onChange={(e) => {
            setter?.answer(answerId, e.target.value);
          }}
          editmode={editmode}
        >
          {children}
        </Input>
      </button>
    </div>
  );
}

export default AnswerButton;
