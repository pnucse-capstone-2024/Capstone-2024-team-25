import { useEffect, useRef } from 'react';

function ExamLayout({ children, wrongQuestions, correctQuestions }) {
  const ref = useRef();

  useEffect(() => {
    if (ref.current) {
      wrongQuestions?.forEach((wrongQuestion) => {
        document
          .getElementById(`${wrongQuestion}.`)
          ?.classList.add(
            'relative',
            'before:w-[5px]',
            'before:absolute',
            'before:left-[2px]',
            'before:h-[150px]',
            'before:top-[-60px]',
            'before:bg-red-500',
            'before:rotate-45',
          );
      });

      correctQuestions?.forEach((correctQuestion) => {
        document
          .getElementById(`${correctQuestion}.`)
          ?.classList.add(
            'relative',
            'before:w-[100px]',
            'before:absolute',
            'before:left-[-45px]',
            'before:h-[100px]',
            'before:top-[-40px]',
            'before:border-red-500',
            'before:border-8',
            'before:rounded-full',
          );
      });
    }
  });

  return (
    <div className="justify flex w-[800px] flex-col items-center justify-start px-20 py-4">
      <div ref={ref} className="flex w-full flex-col">
        {children}
      </div>
    </div>
  );
}

export default ExamLayout;
