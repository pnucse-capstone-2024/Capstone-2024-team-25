export default function openModifyExam(option) {
  let uri = '/modify-exam';

  if (option) {
    const queryString = Object.keys(option)
      .map((key) => `${encodeURIComponent(key)}=${encodeURIComponent(option[key])}`)
      .join('&');
    uri += `?${queryString}`;
  }

  window.open(
    uri,
    '_blank',
    'width=1200,height=900,left=10,top=10,toolbar=no,titlebar=no,menubar=no,location=no,resizable=no',
  );
}
