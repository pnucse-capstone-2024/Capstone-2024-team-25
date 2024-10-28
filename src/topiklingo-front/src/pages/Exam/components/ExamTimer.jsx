import { useEffect, useState } from 'react';

function ExamTimer({ initialTime , isSubmitted, onTimeUp }) {
  const [seconds, setSeconds] = useState(initialTime);
  const [isActive, setIsActive] = useState(true);
  const [isTimeUp, setIsTimeUp] = useState(false);

  useEffect(() => {
    let interval;
    if (isActive && !isSubmitted) {
      interval = setInterval(() => {
        setSeconds(prevSeconds => {
          if (prevSeconds >= 1) {
            return prevSeconds - 1;
          } 
            clearInterval(interval);
            setIsActive(false);
            setIsTimeUp(true);
            alert('Exam Time Up. Automatically submitted.');
            return 0;
          
        });
      }, 1000);
    } else {
      clearInterval(interval);
    }

    return () => clearInterval(interval);
  }, [isActive, isSubmitted]);

  useEffect(() => {
    if (isTimeUp) {
      onTimeUp(isTimeUp);  // Exam 컴포넌트의 상태를 업데이트하는 콜백
    }
  }, [isTimeUp, onTimeUp]);

  useEffect(() => {
    if (isSubmitted) {
      setIsActive(false);
    }
  }, [isSubmitted]);

  const toggleIsActive = () => {
    setIsActive(!isActive);
  };

  const formattedTime = () => {
    const minutes = Math.floor(seconds / 60);
    const remainderSeconds = seconds % 60;
    const displayMinutes = minutes < 10 ? `0${minutes}` : minutes;
    const displaySeconds = remainderSeconds < 10 ? `0${remainderSeconds}` : remainderSeconds;
    return `${displayMinutes}:${displaySeconds}`;
  };

  const getColor = () => {
    if (isTimeUp || isSubmitted) { // isSubmitted 상태도 고려
      return 'red';
    } if (seconds < 600) {
      return 'orange';
    } 
      return 'blue';
    
  };
  
  return (
    <div className="flex items-center justify-between">
      <span className='text-gray-500'>
        Remain Time - {' '}
        <span style={{ color: getColor() }}>
          {isTimeUp || isSubmitted ? 'Time Up' : formattedTime()}
        </span>
      </span>
      <button
        type="button"
        onClick={toggleIsActive}
        disabled={isSubmitted} // isSubmitted가 true일 때 버튼 비활성화
        className={`ml-2 px-3 py-1 rounded-lg ${isActive ? 'bg-red-400 text-white' : 'bg-green-600 text-white'}`}
      >
        {isActive ? 'pause' : 'resume'}
      </button>
    </div>
  );
}


export default ExamTimer;
