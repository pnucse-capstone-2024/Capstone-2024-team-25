/* eslint-disable jsx-a11y/no-redundant-roles */
import { useRef, useState, useEffect } from 'react';
import ReactAudioPlayer from 'react-audio-player';

function CustomAudioPlayer({ src, isSubmit }) {
  const audioPlayerRef = useRef(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [isDoubleSpeed, setIsDoubleSpeed] = useState(false);

  useEffect(() => {
    const audioElement = audioPlayerRef.current?.audioEl.current;

    const preventSeek = (e) => {
      if (Math.abs(currentTime - duration) < 1) {
        e.target.currentTime = duration;
      } else {
        e.target.currentTime = 0;
      }
    };

    const updateTime = () => {
      setCurrentTime(audioElement.currentTime);
    };

    const updateDuration = () => {
      setDuration(audioElement.duration);
    };

    if (audioElement) {
      audioElement.addEventListener('seeked', preventSeek);
      audioElement.addEventListener('timeupdate', updateTime);
      audioElement.addEventListener('loadedmetadata', updateDuration);

      // Auto play when the audio metadata is loaded
      audioElement.addEventListener('loadedmetadata', () => {
        audioElement.play();
        setIsPlaying(true);
      });
    }

    return () => {
      if (audioElement) {
        audioElement.removeEventListener('seeked', preventSeek);
        audioElement.removeEventListener('timeupdate', updateTime);
        audioElement.removeEventListener('loadedmetadata', updateDuration);
      }
    };
  }, [currentTime, duration]);

  const handlePlayPause = () => {
    const audioElement = audioPlayerRef.current.audioEl.current;
    if (audioElement.paused) {
      audioElement.play();
      setIsPlaying(true);
    } else {
      audioElement.pause();
      setIsPlaying(false);
    }
  };

  const handleSpeedToggle = () => {
    const audioElement = audioPlayerRef.current.audioEl.current;
    if (isDoubleSpeed) {
      audioElement.playbackRate = 1;
      setIsDoubleSpeed(false);
    } else {
      audioElement.playbackRate = 2;
      setIsDoubleSpeed(true);
    }
  };

  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
  };

  return (
    <div className="bg-white w-full max-w-[768px] flex justify-start items-center p-4 relative max-h-20 shadow-sm rounded-md">
      <div className="grow">
        <span className="text-lg font-bold text-gray-700">Listening</span><br/>
        <span className="text-base font-medium text-gray-700">Broadcast</span>
        <div className="text-sm text-gray-500">
          {formatTime(currentTime)} / {formatTime(duration)}
        </div>
      </div>
      <button
        type="button"
        className="ml-4 rounded-full bg-[#eff0f9] p-2 cursor-pointer group [&_*]:transition-all [&_*]:duration-150 [&_*]:ease-in"
        onClick={handlePlayPause}
        role="button"
        aria-label='play-pause-button'
        disabled={isSubmit}
      >
        <span className="px-2 py-2 block bg-white rounded-full shadow-md group-hover:bg-rose-500">
          {isPlaying ? (
            <svg xmlns="http://www.w3.org/2000/svg" className="group-hover:fill-white group-hover:stroke-white" width="28" height="28" viewBox="0 0 24 24" strokeWidth="1.5" stroke="#7e9cff" fill="#7e9cff" strokeLinecap="round" strokeLinejoin="round">
              <path stroke="none" d="M0 0h24v24H0z" fill="none" />
              <path d="M5 4h4v16h-4zM15 4h4v16h-4z" />
            </svg>
          ) : (
            <svg xmlns="http://www.w3.org/2000/svg" className="group-hover:fill-white group-hover:stroke-white" width="28" height="28" viewBox="0 0 24 24" strokeWidth="1.5" stroke="#7e9cff" fill="#7e9cff" strokeLinecap="round" strokeLinejoin="round">
              <path stroke="none" d="M0 0h24v24H0z" fill="none" />
              <path d="M7 4v16l13 -8z" />
            </svg>
          )}
        </span>
      </button>
      <div className="relative group">
        <button
          type="button"
          className="rounded-full bg-[#eff0f9] p-2 cursor-pointer group [&_*]:transition-all [&_*]:duration-150 [&_*]:ease-in ml-2"
          onClick={handleSpeedToggle}
          role="button"
          aria-label='speed-toggle-button'
          disabled={isSubmit}
        >
          <span className="px-2 py-2 bg-white rounded-full shadow-md group-hover:bg-indigo-500 group-hover:text-white text-lg font-bold text-gray-700 flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" className="w-6 h-6 mr-1" viewBox="0 0 24 24" fill="#7e9cff">
            <path d="M8 5v14l11-7z" />
            <path d="M2 5v14l11-7z" /> 
            </svg>
            {isDoubleSpeed ? 'x2' : 'x1'}
          </span>
        </button>
        <div className="absolute bottom-full mb-2 left-1/2 transform -translate-x-1/2 bg-gray-700 text-white text-xs rounded-md p-1 opacity-0 group-hover:opacity-100 transition-opacity duration-150">
          {isDoubleSpeed ? 'Normal Speed' : 'Double Speed'}
        </div>
      </div>
      {!isSubmit && (
        <ReactAudioPlayer
          ref={audioPlayerRef}
          src={src}
          controls
          controlsList="nodownload"
          style={{ display: 'none' }}
        />
      )}
    </div>
  );
}

export default CustomAudioPlayer;

// /* eslint-disable react/no-unknown-property */
// import { useRef, useEffect } from 'react';
// import ReactAudioPlayer from 'react-audio-player';

// function CustomAudioPlayer({ src }) {
//   const audioPlayerRef = useRef(null);

//   useEffect(() => {
//     const audioElement = audioPlayerRef.current?.audioEl.current;
    
//     const preventSeek = (e) => {
//       const { currentTime, duration } = e.target;
//       if (Math.abs(currentTime - duration) < 1) {
//         e.target.currentTime = duration;
//       } else {
//         e.target.currentTime = 0;
//       }
//     };

//     if (audioElement) {
//       audioElement.addEventListener('seeked', preventSeek);
//     }

//     return () => {
//       if (audioElement) {
//         audioElement.removeEventListener('seeked', preventSeek);
//       }
//     };
//   }, []);

//   return (
//     <div className="custom-audio-player">
//       <ReactAudioPlayer
//         ref={audioPlayerRef}
//         src={src}
//         controls
//         controlsList="nodownload"
//         style={{ pointerEvents: 'none' }}
//       />
//       <div className="controls">
//         <button type="button" onClick={() => audioPlayerRef.current.audioEl.current.play()} className="play-button">Play</button>
//         <button type="button" onClick={() => audioPlayerRef.current.audioEl.current.pause()} className="pause-button">Pause</button>
//       </div>
//       <style jsx>{`
//         .custom-audio-player {
//           display: flex;
//           flex-direction: column;
//           align-items: center;
//         }
//         .controls {
//           display: flex;
//           gap: 10px;
//         }
//         .play-button, .pause-button {
//           background-color: #1e3a8a;
//           color: white;
//           border: none;
//           padding: 10px 20px;
//           border-radius: 5px;
//           cursor: pointer;
//         }
//         .play-button:hover, .pause-button:hover {
//           background-color: #3b82f6;
//         }
//       `}</style>
//     </div>
//   );
// }

// export default CustomAudioPlayer;