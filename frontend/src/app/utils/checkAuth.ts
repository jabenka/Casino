// utils/checkAuth.ts
const checkAuth = (): boolean => {
    const token = localStorage.getItem('jwtToken');
    return !!token; // Возвращает true, если токен есть, иначе false
};

export default checkAuth;
