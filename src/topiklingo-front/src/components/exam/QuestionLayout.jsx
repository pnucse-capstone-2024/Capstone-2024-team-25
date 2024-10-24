function QuestionLayout({ subitem, title, children }) {
  return (
    <div className="my-2 flex">
      <div className="mr-2 w-fit">{subitem}</div>
      <div className="w-full">
        <h3 className="font-semibold">{title}</h3>
        {children}
      </div>
    </div>
  );
}

export default QuestionLayout;
