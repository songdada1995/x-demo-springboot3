import type { Directive, DirectiveBinding } from 'vue'
import { usePermissionStore } from '../stores/permission'

export const hasPermDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const permissionStore = usePermissionStore()
    const { value } = binding

    if (value && typeof value === 'string') {
      if (!permissionStore.hasPermission(value)) {
        el.parentNode?.removeChild(el)
      }
    } else if (Array.isArray(value)) {
      const hasAny = value.some((perm: string) => permissionStore.hasPermission(perm))
      if (!hasAny) {
        el.parentNode?.removeChild(el)
      }
    }
  },
}
