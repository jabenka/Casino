const checkToken = (): boolean => {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        return false;
    }

    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map((c) => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    const { exp } = JSON.parse(jsonPayload);
    const now = Math.floor(Date.now() / 1000);

    return exp > now;
};

export default checkToken;
