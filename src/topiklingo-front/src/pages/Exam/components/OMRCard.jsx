function OMRCard({ userSelected, setUserSelected, wrongQuestions, correctQuestions }) {
  const checkValue = (questionNumber, selectedValue) => {
    const newUserSelected = [...userSelected];
    newUserSelected[questionNumber] = selectedValue;
    setUserSelected(() => newUserSelected);
  };

  return (
    <div className="top-4 mr-16 h-[485px] w-[300px] min-w-[300px] border-2 border-slate-400">
      <h2 className="flex h-10 items-center justify-center font-bold bg-gray-400 border-gray-500 pb-2">
        {/* <ExamTimer initialTime={3600} /> */}
        OMR Card
      </h2>
      <div className="flex-col h-[440px] w-[300px] overflow-y-scroll">
      {userSelected.slice(1).map((selectedNumber, index) => (
        // eslint-disable-next-line react/no-array-index-key
        <div key={index} className="flex h-10">
          <a href={`#${index + 1}.`} className="relative flex items-center justify-center">
            {wrongQuestions.includes(index + 1) && (
              <div className="absolute left-4 top-2 h-[30px] w-[3px] rotate-45 bg-red-500" />
            )}
            {correctQuestions.includes(index + 1) && (
              <div className="absolute left-1 top-2 h-[30px] w-[30px] rounded-full border-4 border-red-500" />
            )}
            <div className="flex w-10 h-full items-center justify-center pl-1 font-bold border-gray-300 border-r-2 bg-gray-200">{index + 1}</div>
            <div className="flex w-60 h-full items-center justify-around border-b-2 border-dotted">
              {[1, 2, 3, 4].map((answerValue) => (
                <button
                  key={answerValue}
                  type="button"
                  onClick={() => checkValue(index + 1, answerValue)}
                  className="flex h-8 w-6 items-center justify-center rounded-xl border border-gray-200 hover:bg-gray-200"
                >
                  <div
                    className={`flex h-8 w-6 items-center justify-center rounded-xl ${answerValue === selectedNumber ? 'bg-black text-white' : ''}`}
                  >
                    {answerValue}
                  </div>
                </button>
              ))}
            </div>
          </a>
        </div>
      ))}
      </div>
    </div>
  );
}

export default OMRCard;
