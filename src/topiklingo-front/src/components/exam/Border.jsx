function Border({ children, isThin }) {
  return <div className={`w-full border-midnightBlue p-4 ${isThin ? 'border' : 'border-2'}`}>{children}</div>;
}

export default Border;
