const getUserData = async (userId, token) => {
    try {
        const res = await fetch(`${import.meta.env.VITE_API_URL}/member/${userId}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
        });
        if (res.ok) {
            const json = await res.json();
            return json;
        }
        return null;
        } catch (err) {
            console.log(err);
            alert('User Data loading failed. Please try again.');
            return null;
        }
    };

const getUserRecord = async (userId, token) => {
  try {
    const res = await fetch(`${import.meta.env.VITE_API_URL}/exam-record/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (res.ok) {
        const json = await res.json();
        return json;
    }
    return null;
    }catch (err) {
        console.log(err);
        alert('User Data loading failed. Please try again.');
        return null;
    }
};

const getUserAnalyze = async (userId, token) => {
  try {
    const res = await fetch(`${import.meta.env.VITE_API_URL}/analyze/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    if (res.ok) {
        const json = await res.json();
        return json;
    }
    return null;
    } catch (err) {
        console.log(err);
        alert('User Data Analysis failed. Please try again.');
        return null;
    }
};

export { getUserData, getUserRecord, getUserAnalyze };
