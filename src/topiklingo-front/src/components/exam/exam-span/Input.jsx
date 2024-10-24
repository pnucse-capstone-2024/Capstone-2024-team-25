function Input({ number, className, value, children, onChange, editmode }) {
  if (!editmode) {
    return (
      <>
        <span className="mr-2">{number}</span>
        <span className={className}>{children}</span>
      </>
    );
  }

  return (
    <div className="flex flex-col">
      <div className="flex">
        <span className="mr-2">{number}</span>
        <span className={className}>{children}</span>
      </div>
      {editmode && (
        <input
          type="text"
          className="mb-2 w-full rounded-md border border-gray-300 p-2 text-black"
          value={value}
          onChange={onChange}
        />
      )}
    </div>
  );
}

export default Input;
