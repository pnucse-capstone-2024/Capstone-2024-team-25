import { useEffect, useRef } from 'react';

function PrintLayout({ children }) {
  const ref = useRef();

  useEffect(() => {
    if (ref.current) {
      let heightSum = 0;
      let index = 0;
      
      while (index < ref.current.children.length) {
        if (heightSum + ref.current.children[index].clientHeight > 1050) {
          ref.current.children[index].classList.add('print:break-before-page', 'print:pt-12');
          heightSum = 0;
        }
        heightSum += ref.current.children[index].clientHeight;
        index += 1;
      }
    }
  });

  return (
    <div className="justify flex w-[800px] flex-col items-center justify-start border border-gray-300 px-24 py-12">
      <div ref={ref} className="flex w-full flex-col">
        {children}
      </div>
    </div>
  );
}

export default PrintLayout;
