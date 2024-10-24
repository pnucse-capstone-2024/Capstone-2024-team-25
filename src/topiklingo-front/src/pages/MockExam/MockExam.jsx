import { useRef, useState } from 'react';
import { useReactToPrint } from 'react-to-print';

import Topik from '@components/exam/exam-types/Topik';
import PrintLayout from '@components/layouts/PrintLayout';
import PrintTwoLayout from '@components/layouts/PrintTwoLayout';

function MockExam() {
  const componentRef = useRef();
  const handlePrint = useReactToPrint({
    content: () => componentRef.current,
  });

  const [toggleState, setToggleState] = useState([false, false, false, false]);

  return (
    <div className="flex w-full flex-col items-center overflow-y-scroll px-16 pt-8 scrollbar-hide">
      <div className="flex w-[800px] justify-end">
        <button type="button" onClick={handlePrint} className="h-10 w-20 rounded bg-aquaMist text-platinumWhite">
          출력
        </button>
      </div>
      <div>
        <div className="flex w-[800px] justify-start">
          <button
            className="mr-2 rounded bg-aquaMist px-2 text-white"
            type="button"
            onClick={() => {
              setToggleState((prev) => [!prev[0], false, false, false]);
            }}
          >
            TOPIK I 듣기
          </button>
          <button
            className="mr-2 rounded bg-aquaMist px-2 text-white"
            type="button"
            onClick={() => {
              setToggleState((prev) => [false, !prev[1], false, false]);
            }}
          >
            TOPIK I 읽기
          </button>
          <button
            className="mr-2 rounded bg-aquaMist px-2 text-white"
            type="button"
            onClick={() => {
              setToggleState((prev) => [false, false, !prev[2], false]);
            }}
          >
            TOPIK II 듣기
          </button>
          <button
            className="mr-2 rounded bg-aquaMist px-2 text-white"
            type="button"
            onClick={() => {
              setToggleState((prev) => [false, false, false, !prev[3]]);
            }}
          >
            TOPIK II 읽기
          </button>
        </div>
        <div ref={componentRef}>
          {toggleState[0] && (
            <PrintLayout>
              <Topik />
            </PrintLayout>
            // <PrintTwoLayout>
            //   <TopikOneListening />
            // </PrintTwoLayout>
          )}
          {toggleState[1] && (
            // <PrintLayout>
            //   <TopikOneReading />
            // </PrintLayout>
            <PrintTwoLayout>
              <Topik />
            </PrintTwoLayout>
          )}
          {toggleState[2] && (
            // <PrintLayout>
            //   <TopikTwoListening />
            // </PrintLayout>
            <PrintTwoLayout>
              <Topik />
            </PrintTwoLayout>
          )}
          {toggleState[3] && (
            // <PrintLayout>
            //   <TopikTwoReading />
            // </PrintLayout>
            <PrintTwoLayout>
              <Topik />
            </PrintTwoLayout>
          )}
        </div>
      </div>
    </div>
  );
}

export default MockExam;
