import { environment } from '../../../environments/environment';

export function getCompanyLogoUrl(
  logoUrl: string | null | undefined,
  companyDomain: string | null | undefined,
): string | null {
  const storedLogo = logoUrl?.trim();
  if (storedLogo && !storedLogo.startsWith('https://cdn.brandfetch.io/')) {
    return storedLogo;
  }

  // Le domaine évite de garder un lien expiré.
  const domain = companyDomain?.trim();
  const clientId = environment.brandFetch_clientId.trim();

  return domain && clientId
    ? `https://cdn.brandfetch.io/domain/${encodeURIComponent(domain)}/w/28/h/28/fallback/lettermark?c=${encodeURIComponent(clientId)}`
    : null;
}
