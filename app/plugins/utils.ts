export default defineNuxtPlugin(() => {
  function dateFormat(timestamp: number) {
    const date = new Date(timestamp);
    return Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'medium' }).format(date);
  }
  return { provide: { dateFormat } };
});
