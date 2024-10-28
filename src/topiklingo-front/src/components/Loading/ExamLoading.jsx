function ExamLoading({ isLoading, isCBTSelected }) {
  const loadingText = isCBTSelected ? 'Preparing CBT Environment...' : 'Creating PBT Paper...';
  return (
    <div
      aria-label={loadingText}
      role="status"
      className={`fixed inset-0 flex items-center justify-center space-x-2 bg-black bg-opacity-30 ${isLoading ? '' : 'hidden'}`}
    >
      <svg className="h-16 w-16 animate-spin stroke-white md:h-20 md:w-20" viewBox="0 0 256 256">
        <line x1="128" y1="32" x2="128" y2="64" strokeLinecap="round" strokeLinejoin="round" strokeWidth="24" />
        <line x1="195.9" y1="60.1" x2="173.3" y2="82.7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="24" />
        <line x1="224" y1="128" x2="192" y2="128" strokeLinecap="round" strokeLinejoin="round" strokeWidth="24" />
        <line
          x1="195.9"
          y1="195.9"
          x2="173.3"
          y2="173.3"
          strokeLinecap="round"
          strokeLinejoin="round"
          strokeWidth="24"
        />
        <line x1="128" y1="224" x2="128" y2="192" strokeLinecap="round" strokeLinejoin="round" strokeWidth="24" />
        <line x1="60.1" y1="195.9" x2="82.7" y2="173.3" strokeLinecap="round" strokeLinejoin="round" strokeWidth="24" />
        <line x1="32" y1="128" x2="64" y2="128" strokeLinecap="round" strokeLinejoin="round" strokeWidth="24" />
        <line x1="60.1" y1="60.1" x2="82.7" y2="82.7" strokeLinecap="round" strokeLinejoin="round" strokeWidth="24" />
      </svg>
      <span className="text-3xl font-medium text-white md:text-4xl">{loadingText}</span>
    </div>
  );
}

export default ExamLoading;
