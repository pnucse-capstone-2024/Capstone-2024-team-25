import PropTypes from 'prop-types';

import PastExamCard from './PastExamCard';

function PastExamRow({ title }) {
  return (
    <div className="mb-20 flex h-72 flex-col justify-between rounded p-2">
      <h3 className="text-2xl font-bold">{title}</h3>
      <div className="flex justify-between">
        <PastExamCard />
        <PastExamCard />
        <PastExamCard />
        <PastExamCard />
      </div>
    </div>
  );
}

PastExamCard.defaultProps = {
  title: '기출 문제 제목',
};

PastExamCard.propTypes = {
  title: PropTypes.string.isRequired,
};

export default PastExamRow;
