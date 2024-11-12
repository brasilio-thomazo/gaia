export function usePrincipal() {
  return useState<User | null>('principal', () => null);
}
