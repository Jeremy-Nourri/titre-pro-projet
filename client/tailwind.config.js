/** @type {import('tailwindcss').Config} */
export default {
    darkMode: ["class"],
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            fontFamily: {
                title: ['PoppinMedium', 'sans-serif'],
                body: ['Roboto', 'sans-serif'],
            },
            colors: {
                primary: "#2f302f",
                secondary: "#63666a",
                bluecolor: "#004aad",
                danger: "#EF4444",
            },
            
        }
    },
    plugins: [import("tailwindcss-animate")],
}

