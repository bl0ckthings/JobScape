import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const token = sessionStorage.getItem('token');

  if (!token) return next(req);

  const requestUrl = new URL(req.url, window.location.origin);
  const apiUrl = new URL(environment.apiBaseUrl, window.location.origin);

  if (requestUrl.origin !== apiUrl.origin) {
    return next(req);
  }

  const jwtReq = req.clone({
    setHeaders: { Authorization: `Bearer ${token}` },
  });
  return next(jwtReq);
};
