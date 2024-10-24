function PageHeader({ title }) {
  return (
    <>
      <div className="flex h-28 w-full items-center px-6 py-2 text-2xl font-semibold md:text-3xl">{title}</div>
      <hr className="h-[0.1rem] w-[95%] self-center border-0 bg-midnightBlue" />
    </>
  );
}

export default PageHeader;
