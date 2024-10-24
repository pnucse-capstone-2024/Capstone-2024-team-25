function PastExamCard() {
  const openExam = () => {
    window.open(
      'exam',
      '_blank',
      'width=1200,height=900,left=10,top=10,toolbar=no,titlebar=no,menubar=no,location=no,resizable=no',
    );
  };

  return (
    <div className="h-48 w-48 border-2 border-slate-400">
      <div className="flex h-4/5 items-center justify-center border-b border-gray-200">한국어능력시험</div>
      <div className="flex h-1/5 items-center justify-end px-4">
        <button type="button" onClick={openExam}>
          문제풀기 &gt;
        </button>
      </div>
    </div>
  );
}

export default PastExamCard;
