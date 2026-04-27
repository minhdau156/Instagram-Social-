import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { CaptionEditor } from './CaptionEditor';

describe('CaptionEditor', () => {
    it('renders textfield and character counter', () => {
        const handleChange = vi.fn();
        render(<CaptionEditor value="" onChange={handleChange} />);
        
        expect(screen.getByPlaceholderText(/Write a caption/i)).toBeInTheDocument();
        expect(screen.getByText('2200')).toBeInTheDocument();
    });

    it('calculates remaining characters correctly', () => {
        const handleChange = vi.fn();
        render(<CaptionEditor value="Hello" onChange={handleChange} />);
        
        expect(screen.getByText('2195')).toBeInTheDocument(); // 2200 - 5 = 2195
    });

    it('calls onChange when user types', () => {
        const handleChange = vi.fn();
        render(<CaptionEditor value="" onChange={handleChange} />);
        
        const input = screen.getByPlaceholderText(/Write a caption/i);
        fireEvent.change(input, { target: { value: 'New text' } });
        
        expect(handleChange).toHaveBeenCalledWith('New text');
    });
});
