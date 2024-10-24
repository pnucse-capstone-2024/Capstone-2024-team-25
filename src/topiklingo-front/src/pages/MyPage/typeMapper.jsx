/* eslint-disable no-nested-ternary */
import { allProblemTypes } from '@constants/topik-problem-types';

function typeMap ({type}) {
    const problemType = `${type.split('_')[0]  }_${  type.split('_')[1]}`;
    const problemId = `${type.split('_')[2]  }_${  type.split('_')[3]}_${  type.split('_')[4]}`
    const problemTypeInfo = allProblemTypes ? allProblemTypes[type.toLowerCase()] : null;
    const problemTypeTitle = problemTypeInfo ? problemTypeInfo.title : 'UNKNOWN';

    const colorClass = problemType === 'READING_1'
    ? 'text-orange-700' // Example for READING_1
    : problemType === 'READING_2'
    ? 'text-teal-700' // Example for READING_2
    : problemType === 'LISTEN_1'
    ? 'text-orange-600' // Example for LISTEN_1
    : problemType === 'LISTEN_2'
    ? 'text-teal-600' // Example for LISTEN_2
    : 'text-gray-600'; // Default color

    return (
      <>
      <h3 className={`text-base sm:text-xl font-semibold mb-2 ${colorClass}`}>
        {problemType}<br />
        <span className="text-lg text-blue-600">{problemId}</span>
      </h3>
      <p className='text-gray-700 mb-2 text-sm font-semibold sm:text-base'>{problemTypeTitle}</p>
    </>
    );
  }

function shortenType({type}){
  const problemType = `${type.split('_')[0]  }_${  type.split('_')[1]}`;
  const problemTypeInfo = allProblemTypes ? allProblemTypes[type.toLowerCase()] : null;
  const problemTypeTitle = problemTypeInfo ? problemTypeInfo.title : 'UNKNOWN';

  return [problemType, problemTypeTitle]
}

export default typeMap;
export {shortenType};