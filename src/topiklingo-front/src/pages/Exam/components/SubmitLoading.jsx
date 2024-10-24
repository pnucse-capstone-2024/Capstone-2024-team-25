function SubmitLoading(){
    return (
      <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
        <div className="flex flex-col items-center justify-center bg-white p-8 rounded-lg shadow-lg">
        <img className="w-20 h-20 animate-spin" src="https://www.svgrepo.com/show/474682/loading.svg" alt="Loading icon"/>
          <h2 className="text-xl font-semibold">Grading...</h2>
          <p className="text-gray-500">Please wait while we submit the exam.</p>
        </div>
      </div>
    );
  }

export default SubmitLoading;