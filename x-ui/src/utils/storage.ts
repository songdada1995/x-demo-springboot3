const PREFIX = 'x-ui_'

export const storage = {
  set(key: string, value: any): void {
    const data = JSON.stringify(value)
    localStorage.setItem(PREFIX + key, data)
  },

  get(key: string): any {
    const data = localStorage.getItem(PREFIX + key)
    if (data) {
      try {
        return JSON.parse(data)
      } catch (e) {
        return data
      }
    }
    return null
  },

  remove(key: string): void {
    localStorage.removeItem(PREFIX + key)
  },

  clear(): void {
    localStorage.clear()
  }
} 