import { useEffect, useRef } from 'react';

function PrintTwoLayout({ children }) {
  const ref = useRef(null);

  useEffect(() => {
    if (ref && ref.current) {
      Array.from(ref.current.children).map(
        (child) => child.classList.add('w-[400px]', 'px-6'), // 'border-r', 'border-midnightBlue'
      );
    }
  }, [children]);

  return (
    <div className="justify flex w-[800px] flex-col flex-wrap items-start justify-start">
      <div ref={ref} className="flex h-[1132px] flex-col flex-wrap overflow-x-visible py-12 text-sm scrollbar-hide">
        {children}
      </div>
    </div>
  );
}

export default PrintTwoLayout;
