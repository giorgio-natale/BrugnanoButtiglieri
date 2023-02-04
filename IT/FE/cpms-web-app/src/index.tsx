import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {RouterProvider} from "react-router-dom";
import {getRouter} from "./router/Router";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {configApiDefault} from "./api/ApiConfig";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const queryClient = new QueryClient();
const router = getRouter({queryClient});
configApiDefault();

root.render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router}/>
    </QueryClientProvider>
  </React.StrictMode>
);