/* eslint-disable react/jsx-props-no-spreading */
import { useEffect } from 'react';

import topikProblemTypes from '@constants/topik-problem-types';
import useEditExam from '@hooks/useEditExam';

function ProblemLoader({ Component, ...props }) {
  if (!Component) return null;
  return <Component editmode {...props} />;
}

function TopikEditor({ originConfig, setConfig, answers }) {
  const { setExam, getter, setter, texts, getEditedConfig } = useEditExam(originConfig);

  useEffect(() => {
    if (originConfig) {
      setExam(originConfig);
    }
  }, [originConfig]);

  useEffect(() => {
    if (texts) {
      setConfig(getEditedConfig());
    }
  }, [texts]);

  if (!originConfig) return null;

  return (
    <>
      {Object.entries(originConfig).map(([key, value]) =>
        value.map((pb) => (
          <ProblemLoader
            key={pb.problemId}
            Component={topikProblemTypes[key]?.Component}
            config={pb}
            getter={getter}
            setter={setter}
            userSelected={answers}
            setUserSelected={() => {}}
          />
        )),
      )}
    </>
  );
}

export default TopikEditor;
