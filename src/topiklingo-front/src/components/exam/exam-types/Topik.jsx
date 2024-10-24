/* eslint-disable react/jsx-props-no-spreading */
// import config from './topikTwo.config';
import topikProblemTypes from '@constants/topik-problem-types';

function ProblemLoader({ Component, ...props }) {
  if (!Component) return null;
  return <Component {...props} />;
}

function Topik({ config, userSelected, setUserSelected }) {
  if (!config) return null;

  return (
    <>
      {Object.entries(config).map(([key, value]) =>
        value.map((pb) => (
          <ProblemLoader
            key={pb.problemId}
            Component={topikProblemTypes[key]?.Component}
            config={pb}
            userSelected={userSelected}
            setUserSelected={setUserSelected}
          />
        )),
      )}
    </>
  );
}

export default Topik;
