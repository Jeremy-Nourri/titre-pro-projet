import pluginVue from 'eslint-plugin-vue';
import vueTsEslintConfig from '@vue/eslint-config-typescript';
import pluginVitest from '@vitest/eslint-plugin';
import skipFormatting from '@vue/eslint-config-prettier/skip-formatting';

export default [
    {
        name: 'app/files-to-lint',
        files: ['**/*.{ts,mts,tsx,vue,js}'],
    },
    {
        name: 'app/files-to-ignore',
        ignores: ['**/dist/**', '**/dist-ssr/**', '**/coverage/**', '**/node_modules/**', '**/*.d.ts'],
    },
    ...pluginVue.configs['flat/recommended'],
    ...vueTsEslintConfig(),
    {
        ...pluginVitest.configs.recommended,
        files: ['src/**/__tests__/*'],
    },
    skipFormatting,
    {
        rules: {
            indent: ['error', 4],
            'vue/html-indent': ['error', 4],
        },
    },
];
