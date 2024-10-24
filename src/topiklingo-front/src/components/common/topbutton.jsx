/* eslint-disable jsx-a11y/control-has-associated-label */

function TopButton() {
  const showButton = true;

  const handleClick = () => {
    // 첫 번째 앵커로 이동
    const firstAnchor = document.querySelector('#page_top');
    if (firstAnchor) {
      firstAnchor.scrollIntoView({ behavior: 'smooth' });
      firstAnchor.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // 1초 후 두 번째 앵커로 이동
    // setTimeout(() => {
    //   const secondAnchor = document.querySelector('#main_top');
    //   if (secondAnchor) {
    //     secondAnchor.scrollTo({ top: 0, behavior: 'smooth' });
    //   }
    // }, 1000);
  };

  return showButton ? (
    <div className="flex fixed bottom-10 right-10">
      <button
        type="button"
        onClick={handleClick}
        className="p-2 text-white bg-indigo-600 rounded-full hover:bg-indigo-700 focus:outline-none focus:ring-4 focus:ring-indigo-300"
      >
        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 15l7-7 7 7" />
        </svg>
      </button>
    </div>
  ) : null;
}

export default TopButton;