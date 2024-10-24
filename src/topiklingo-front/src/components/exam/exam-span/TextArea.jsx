function TextArea({ className, children, value, onChange, editmode = false }) {
  if (!editmode) {
    return <><span className={className} dangerouslySetInnerHTML={{ __html: children }} /><br /></>;
  }

  return (
    <div>
      <span className={className} dangerouslySetInnerHTML={{ __html: children }} />
      {editmode && (
        <textarea
          className="mt-2 h-40 w-full resize-none rounded-md border border-gray-300 p-2"
          value={value}
          onChange={onChange}
        />
      )}
    </div>
  );
}

export default TextArea;
