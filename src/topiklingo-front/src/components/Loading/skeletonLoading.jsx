function SkeletonLoader() {
  return (
    <div className="animate-pulse">
      <div className="h-4 bg-gray-300 rounded w-3/4" />
      <div className="mt-4 h-4 bg-gray-300 rounded w-5/6" />
      <div className="mt-4 h-4 bg-gray-300 rounded w-2/3" />
    </div>
  );
}

export default SkeletonLoader;