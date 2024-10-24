import Border from './Border';

function ExampleLayout({ children }) {
  return (
    <Border>
      <div className="flex w-full justify-center text-charcoalGray">&lt;보기&gt;</div>
      {children}
    </Border>
  );
}

export default ExampleLayout;
