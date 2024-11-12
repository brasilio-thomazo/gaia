export default defineNuxtRouteMiddleware(async (to, from) => {
  const principal = usePrincipal();
  const token = useCookie('token');
  const { $auth } = useNuxtApp();
  if (!token.value) {
    return navigateTo('/login');
  }
  if (!$auth.validate(token.value)) {
    return navigateTo('/login');
  }
  if (!principal.value) {
    try {
      principal.value = await $auth.me();
    } catch (error) {
      return navigateTo('/login');
    }
  }
});
