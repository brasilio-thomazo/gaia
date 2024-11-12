// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: true },
  css: ['~/assets/css/main.css'],
  modules: ['@nuxt/icon', '@nuxtjs/i18n'],
  i18n: {
    locales: [{ code: 'pt-BR', dir: 'auto', file: 'pt_BR.json' }],
    defaultLocale: 'pt-BR',
    langDir: '',
    lazy: true
  }
});
