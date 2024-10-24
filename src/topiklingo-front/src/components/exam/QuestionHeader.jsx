const sizeMap = {
  sm: '',
  md: 'font-semibold',
  lg: 'font-bold text-lg mb-6',
};

function QuestionHeader({ subItem, title, size = 'md' }) {
  return (
    <div className={`flex w-full ${sizeMap[size]}`}>
      <div id={`${subItem}`} className="mr-4 w-4">
        {subItem}
      </div>
      <h3 className="w-full">{title}</h3>
    </div>
  );
}

export default QuestionHeader;
