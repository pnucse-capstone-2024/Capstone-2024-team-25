import AUTH_LIST from '@constants/authList';

export default function openExam(option, userAuth = 'STUDENT', needAuth = 'STUDENT') {
  let uri = '/exam';

  const authIdx = needAuth ? AUTH_LIST.indexOf(needAuth) : -1;
  const userAuthIdx = userAuth ? AUTH_LIST.indexOf(userAuth) : AUTH_LIST.length;

  if (authIdx < userAuthIdx) {
    alert('You do not have permission. Please Login First.');
  } else {
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
}
