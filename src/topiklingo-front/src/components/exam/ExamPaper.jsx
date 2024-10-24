function ExamPaper({ children }) {
  return (
    <div className="justify flex h-[1132px] w-[800px] flex-col items-center justify-start border border-midnightBlue px-24 py-12">
      {children}
    </div>
  );
}

export default ExamPaper;
