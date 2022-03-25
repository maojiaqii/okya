<template>
  <el-button :size="size" :type="type" :disabled="disabled"
             :loading="loading" v-if="hasPerms(perms)" @click="handleClick" :icon="icon" plain>
      <slot v-bind:label="label">
        {{ label }}
      </slot>
  </el-button>
</template>

<script>
    export default {
        data() {
            return {
                label: this.btnName,
                icon: this.btnIcon,
                key: ''
            }
        },
        props: {
            btnName: {
                type: String,
                default: null
            },
            size: {
                type: String,
                default: 'small'
            },
            type: {
                type: String,
                default: null
            },
            loading: {
                type: Boolean,
                default: false
            },
            disabled: {
                type: Boolean,
                default: false
            },
            perms: {
                type: String,
                default: null
            },
            btnIcon: {
                type: String,
                default: null
            }
        },
        methods: {
            handleClick: function () {
                this.$emit('click', {})
            },
            hasPerms: function (perms) {
                let allButtons = JSON.parse(this.$store.state.app.buttons)
                for (let i = 0; i < allButtons.length; i++) {
                    if (allButtons[i].compo == perms) {
                        this.label = this.label == null ? allButtons[i].compoName : this.label
                        this.icon = this.icon == null ? allButtons[i].icon : this.icon
                        return true;
                    }
                }
                return false;
            }
        },
        mounted() {
        }
    }
</script>

<style scoped>

</style>
