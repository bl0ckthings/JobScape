import { routes } from './app.routes';

describe('app routes', () => {
  it('keeps Candidatures and Dashboard on separate paths', () => {
    const shell = routes.find(route => route.path === '');

    expect(shell?.children?.some(route => route.path === 'applications')).toBe(true);
    expect(shell?.children?.some(route => route.path === 'dashboard')).toBe(true);
    expect(shell?.children?.find(route => route.path === '')?.redirectTo).toBe('applications');
  });
});
