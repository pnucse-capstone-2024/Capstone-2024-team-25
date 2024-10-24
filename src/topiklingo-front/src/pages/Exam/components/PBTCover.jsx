import { SlHourglass } from 'react-icons/sl';

function PBTCover({ id, year, examType, subject, title, timeLimit }) {
  return (
    <div className="flex flex-col justify-between h-screen w-full px-4 md:px-8">
      <header className="flex items-end justify-end h-16 md:h-20 border-b-2 border-midnightBlue text-3xl md:text-4xl font-semibold">
        TOPIK
      </header>
      <div className="flex flex-col items-center justify-around h-full w-full md:h-[35rem]">
        <div className="flex flex-col items-center justify-center w-11/12 md:w-1/2 h-5/6">
          <div className="text-3xl md:text-5xl font-bold text-midnightBlue mb-5">제 {year}년</div>
          <div className="text-3xl md:text-5xl font-bold text-midnightBlue">한국어능력시험</div>
          <div className="text-3xl md:text-5xl font-bold text-midnightBlue">모의고사</div>
          <div className="font-medium text-midnightBlue text-xl my-8">{title}</div>
          <div className="font-light text-charcoalGray my-8">The {year} Mock Test of Proficiency in Korean</div>
          <div className="font-light text-charcoalGray my-8">ExamID : {id}</div>
          <div className="flex flex-col justify-between h-32 w-full border-y border-midnightBlue mb-10">
            <div className="flex items-center justify-center h-1/2 w-full border-b border-charcoalGray border-opacity-30 text-lg md:text-xl font-bold">
              <span className="w-24 md:w-32 text-center">TOPIK</span>
              <span className="w-24 md:w-32 border-x border-charcoalGray border-opacity-30 text-center">{examType}</span>
              <span className="w-24 md:w-32 text-center">{subject}</span>
            </div>
            <div className="flex items-center justify-center h-1/2 w-full text-md md:text-lg font-light">
              <SlHourglass />
              제한 시간<span className="mx-1 text-lg md:text-2xl font-bold">{timeLimit}</span>분
            </div>
          </div>
        </div>
      </div>
      <footer className="h-16 md:h-20 border-t-2 border-midnightBlue text-xl md:text-2xl font-bold flex items-end justify-between">
        <div className="flex-1">
          <span className='text-left'>
          도서 출판 참
          </span>
        </div>
        <div className="flex-1 text-right">
          <span>
          TheGrapeBine
          </span>
        </div>
      </footer>
      
    </div>
  );
}

export default PBTCover;