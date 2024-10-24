/* eslint-disable jsx-a11y/label-has-associated-control */
import { useState } from 'react';

import { shortenType } from '@pages/MyPage/typeMapper';
import { ResponsiveContainer, BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Bar, LabelList, Legend } from 'recharts';

function FilterableBarChart({ userAnalyze }) {
  const uniqueProblemTypes = [
    ...new Set(
      userAnalyze.map(analyze => {
        const typeParts = analyze.problemType.split('_');
        return `${typeParts[0]}_${typeParts[1]}`;  // 'READING_1' 추출
      })
    )
  ];

  const [selectedProblemType, setSelectedProblemType] = useState(uniqueProblemTypes[0]);

  const filteredData = userAnalyze
    .filter(analyze => {
      const typeParts = analyze.problemType.split('_');
      const problemType = `${typeParts[0]}_${typeParts[1]}`;
      return problemType === selectedProblemType;
    })
    .map(analyze => {
      const [problemType, problemTypeTitle] = shortenType({ type: analyze.problemType });
      const accuracy = ((analyze.correctCount / analyze.totalCount) * 100).toFixed(2); // 정답률 계산
      const incorrectCount = analyze.totalCount - analyze.correctCount; // 틀린 문제 수 계산
      return {
        ...analyze,
        displayType: `${problemTypeTitle}`, // 문제 유형 제목
        accuracy,  // 정답률 추가
        incorrectCount, // 틀린 문제 수 추가
      };
    });

  return (
    <div>
      <label htmlFor="problemType">Select Exam Type: </label>
      <select
        id="problemType"
        value={selectedProblemType}
        onChange={e => setSelectedProblemType(e.target.value)}
      >
        {uniqueProblemTypes.map(problemType => (
          <option key={problemType} value={problemType}>
            {problemType}
          </option>
        ))}
      </select>

      {/* Bar Chart */}
      <ResponsiveContainer width="100%" height={filteredData.length * 50}>
        <BarChart
          layout="vertical"
          data={filteredData}
          margin={{ top: 0, right: 100, left: 30, bottom: 10 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis type="number" tick={{ fontSize: 12 }} label={{ value: '문제 개수 (Total)', position: 'bottom', offset: -10}} />
          <YAxis type="category" dataKey="displayType" tick={{ fontSize: 14 }} interval={0}/>
          <Tooltip />

          {/* 범례 추가 */}
          <Legend verticalAlign="top" height={36} />
          <Bar name="Correct Count" dataKey="correctCount" fill="#82ca9d" stackId="a">
            {/* LabelList to display accuracy inside the bars */}
            <LabelList dataKey="accuracy" position="insideLeft" fill="#000000" fontSize={12} formatter={(value) => `Correct Rate ${value}%`} />
          </Bar>
          {/* Bar for incorrect and correct counts */}
          <Bar name="Incorrect Count" dataKey="incorrectCount" fill="#ff7f7f" stackId="a">
            <LabelList dataKey="incorrectCount" position="insideRight" fill="#ffffff" fontSize={12} />
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

export default FilterableBarChart;
